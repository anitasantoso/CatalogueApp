# Code Challenge
## Scenario
An entrepreneur has big plans and have determined that a custom native app needs to be built. The MVP however, is rather straightforward - a catalogue site.

Customers will browse the catalogue and view product details when they tap the product tile. The entrepreneur has done the market research
to validate the opportunity, and they are in the process of getting creative work to nail the look and feel but really want to get started on the build.
They have provided some acceptance criteria for the basic features

The entrepreneur is looking to you to give them a proof of concept that they can interact with to start firming up the vision for their idea. They are not too fussed with the specific list of products 
at this point so using [this Fake Store API](https://fakestoreapi.com/) is acceptable. But they have been down this road before with other developers and have specified
they don't want a Proof of Concept that needs to be thrown away, they want something they know can grow and evolve with the vision.

## Expectations

This challenge is designed to build tension between the time-box and the amount of requirements you satisfy. But if you
decide to drop certain requirements, please ensure you can justify the decision

1. Raise a Pull Request with a working Android or iOS app (based on the role you are applying for)
2. Make decisions to showcase your strengths
3. Time-box the effort to about 2 hours
4. Focus on producing the critical path
5. Please detail any setup instructions to make it easier for the reviewer to run your application

## Selection Criteria

* Solution Design
* Standard and Quality of the implementation

## Acceptance Criteria
### Home Screen
```
GIVEN I want to browse the available products
 WHEN I am viewing the home screen
 THEN I a list of products
  AND those products are sorted by their rating descending
```

### Category Screen
```
GIVEN my products are organised into categories
  AND I want to see products in a specific category
 WHEN I tap a category button
 THEN I see products in that category
```

### Product Screen
```
GIVEN I am browsing the products (Home screen or Category screen)
  AND I want to see more details about a specific product
 WHEN I tap a product tile
 THEN I see the product details screen with more detailed information about that product
```