describe("a greeting service", function() {
    var client = new HttpClient('http://localhost:9777/');

    it("display the greeting to the user", function() {
        return client
            .get('greeting.html')
            .then(function(document) {
                expect($(document).find('#greeting').text()).to.be.equal('Hello World!');
            });
    });

    it("respond with a greeting", function() {
        var data = { name: 'Mantas' };

        return client
            .post('api/greeting', data)
            .then(function(greeting) {
                expect(greeting.text).to.be.equal('Hello, Mantas');
            });
    });

    it("handle a 404", function() {
        return client
            .get('xyz.html')
            .catch(function(error) {
                expect(error).to.be.equal('Not Found');
            });
    });

    it("handle a connection error", function() {
        return new HttpClient('http://xyzdef:65535/')
            .get('xyz.html')
            .catch(function(error) {
                expect(error).to.be.equal('Connection refused');
            });
    });

    it("be a failed try", function() {
        expect(true).to.be.true;
    });
});