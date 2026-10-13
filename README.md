                    ┌──────────────────────────────┐
                    │        Adapters IN           │
                    │                              │
                    │ REST Controller              │
                    │ CLI / Tests                  │
                    └──────────────┬───────────────┘
                                   │
                                   ▼
                    ┌──────────────────────────────┐
                    │        APPLICATION           │
                    │                              │
                    │ CreateOrder                  │
                    │ AddProductToCart             │
                    │ RemoveProductFromCart         │
                    │ Checkout                      │
                    └──────────────┬───────────────┘
                                   │
                     ┌─────────────┴─────────────┐
                     │                           │
                     ▼                           ▼
              ┌──────────────┐           ┌──────────────┐
              │    DOMAIN    │           │     PORTS    │
              │              │           │              │
              │ Cart         │           │ ProductCatalog│
              │ Order        │           │ OrderRepository│
              │ Product      │           │ Payment       │
              │ Promotions   │           │ Notification  │
              │ Pricing      │           └──────┬───────┘
              └──────────────┘                  │
                                                ▼
                                  ┌────────────────────────┐
                                  │     Adapters OUT       │
                                  │                        │
                                  │ JPA                    │
                                  │ InMemory                │
                                  │ Stripe/PayPal (later)  │
                                  │ Email                  │
                                  │ External Catalog       │
                                  └────────────────────────┘


# The key rule is:
Domain knows nothing about Spring, JPA, HTTP, PostgreSQL, PayPal, email, etc.

1. Découpage des dossiers (Structure Hexagonale)
On sépare le projet en 3 couches distinctes : domain, application (ou use cases), et infrastructure.
2. Étapes d'implémentation du Domain (Cœur Métier)
Money : Value Object pour gérer les montants et devises sans erreurs d'arrondis (BigDecimal).
Cart & CartItem : Gère l'ajout, le retrait et le nettoyage automatique des produits à quantité 0.
3. Orchestration applicative (Service / Use Case)
4. Adaptateurs d'infrastructure & Configuration Spring
5. Stratégie de Test (TDD)

//Money : Value Object pour gérer les montants et devises sans erreurs d'arrondis (BigDecimal)
//Cart & CartItem : Gère l'ajout, le retrait et le nettoyage automatique des produits à quantité 0
//L'utilisation d'un type dédié comme ProductId au lieu d'une simple String s'appuie sur le concept de Strongly Typed ID 
(ou Value Object en Domain-Driven Design).



Étape 1 : catalogue et panier
Les choix

1. Money est un Value Object et non un double. Les double créent des erreurs d'arrondi. Money encapsule un BigDecimal à 2 décimales et garantit « jamais négatif » en un seul endroit.

2. Le Cart ne stocke que ProductId → quantité. L'énoncé le demande : le panier référence les produits sans connaître leur prix. Le prix vient du catalogue au moment du calcul, ce qui évite d'avoir un prix périmé.

3. Qui vérifie « le produit existe dans le catalogue » ? Pas le Cart, car il ne doit pas dépendre du catalogue. C'est le service applicatif qui orchestre : il interroge CatalogPort, puis appelle cart.add(...). Le domaine reste pur, et le catalogue reste remplaçable.

4. Les invariants vivent dans le Cart. Quantité > 0 à l'ajout, retrait impossible au-delà de la quantité présente, ligne supprimée à 0. Aucune couche supérieure ne peut les contourner.

5. Choix à assumer. Retirer plus d'exemplaires qu'il n'y en a lève une exception (plutôt que de ramener silencieusement à 0). Cela évite de masquer une erreur de l'appelant.



Étape 2 : le PricingService
Les choix

1. Le domaine ne parle pas au catalogue. CatalogPort est dans application, et le domaine n'a pas le droit de l'importer. Le CartService résout donc les ProductId en Product via le catalogue, construit des CartLine (produit complet + quantité), et les donne au PricingService. Celui-ci est une fonction pure : des lignes en entrée, un résultat en sortie.

2. Deux niveaux de règles, pas un seul. Ma feuille de route parlait d'une interface PricingRule unique. En regardant l'énoncé, les règles n'agissent pas à la même granularité :

Niveau	Règles	Entrée
Ligne	% produit, montant fixe, 3 achetés = 1 offert	une ligne (prix unitaire, quantité)
Panier	Premium (-10 %), code promo	le sous-total

Elles ont des signatures différentes. « 3 achetés, 1 offert » a besoin de la quantité et du prix unitaire, tandis que Premium n'a besoin que d'un montant. Deux interfaces évitent de forcer un type unique qui serait trop pauvre ou trop riche selon le cas.

3. L'ordre de la liste = l'ordre d'application. L'énoncé impose que Premium s'applique « après application des promotions sur les produits ». Cette contrainte est garantie par la structure : les règles ligne passent avant les règles panier. À l'intérieur d'un niveau, l'ordre de la liste fait foi. C'est une décision métier, que l'on configurera à l'assemblage et non dans le code des règles.

4. Un objet résultat détaillé (PricingResult). Il contient le total par ligne, le sous-total et le total final. On en aura besoin à l'étape 5 pour figer les prix dans la commande.

5. Aucune règle concrète pour l'instant. Avec deux listes vides, le service calcule simplement prix × quantité. C'est l'étape « total sans promotion » de la feuille de route. L'extensibilité sera prouvée par un test avec une règle bidon.