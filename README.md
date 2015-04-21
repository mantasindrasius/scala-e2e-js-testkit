## Testing Scala project End-to-End

This is a POC for testing Scala project End-to-End using JavaScript + Karma + Mocha specs syntax.

### Purpose

See my presentation here: http://talks.indrasius.lt/js-e2e

### Usage example

```scala
package lt.indrasius.products.it

import lt.indrasius.e2e.js.{KarmaRunner, MochaSpec}

/**
 * Created by mantas on 15.3.23.
 */
class ProductAPISpec extends MochaSpec("e2e/product-api-spec.js") with KarmaRunner {
  config("baseUrl" -> s"http://localhost:${EmbeddedEnvironment.SERVER_PORT}/api/products/")

  bowerInclude("promise-js", "underscore")
  classPathInclude("e2e/http-client.js")
}
```

See the bowerInclude and classPathInclude for including the static files into the Karma context.

The file should reside in src/test/resources/e2e/product-api-spec.js 

```javascript
describe("a product service", function() {
    var http = new HttpClient(jsExecConfig.baseUrl);

    it("store and retrieve product data", function() {
        var givenProductId = "XYZDEF";

        return givenProductExists(givenProductId, "Hello", "212 EUR")
            .then(function() {
                return http.get(givenProductId)
            })
            .then(function(gotResult) {
                expect(gotResult.name).to.be.equal("Hello");
                expect(gotResult.price).to.be.equal("212 EUR");
            });
    });

    it("get all the products", function() {
        return givenRandomProductsExist(3)
            .then(function(productsInStock) {
                return http.get("")
                    .then(function(gotResult) {
                        expect(gotResult).to.haveBodyThatContains(productsInStock)
                    });
            });
    });
});
```

### Running from IDE

Simply Right click on the MochaSpec in Scala or use an appropiate shortcut.

### Running from SBT

```scala
libraryDependencies += "lt.indrasius" %% "js-e2e-testkit" % "1.0-SNAPSHOT" % "test"

testFrameworks += TestFramework("lt.indrasius.e2e.js.sbt.Framework")
```
