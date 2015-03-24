describe("a greeting service", function() {
    var client = new HttpClient('http://localhost:9777/');

    it("display the greeting to the user", function(done) {
        return client
            .get('greeting.html')
            .then(function(document) {
                expect($(document).find('#greeting').text()).to.be.equal('Hello World!');
            });
    });
});