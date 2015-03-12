Browser = require('zombie');
var expect = require('chai').expect;

Browser.localhost('localhost', 9777);

describe("a greeting service", function() {
    var browser = new Browser();

    it("display the greeting to the user", function() {
        return browser
            .visit('/greeting.html')
            .then(jquerify)
            .then(function($) {
                expect($('#greeting').text()).to.be.equal('Hello World!');
            });
    });

    /*it("respond with a greeting", function() {
        var data = { name: 'Mantas' };

        return client
            .post('api/greeting', data)
            .then(function(greeting) {
                expect(greeting.text).to.be.equal('Hello, Mantas');
            });
    });*/

    it("handle a 404", function() {
        return browser
            .visit('/xyz.html')
            .catch(function(error) {
                expect(error).to.be.equal('Not Found');
            });
    });

    it("handle a connection error", function() {
        Browser = require('zombie');
        Browser.localhost('localhost', 65535);

        return new Browser
            .visit('xyz.html')
            .catch(function(error) {
                expect(error).to.be.equal('Connection refused');
            });
    });

    function jquerify() {
        return require('jquery')(browser.window);
    }
});