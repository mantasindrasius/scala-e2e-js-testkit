describe("a greeting service", function() {
    var client = new HttpClient('http://localhost:9777/');

    it("display the greeting to the user", function(done) {
        client
            .get('greeting.html')
            .then(function(document) {
                expect($(document).find('#greeting').text()).toBe('Hello World!');
                done();
            });
    });

    it("respond with a greeting", function(done) {
        var data = { name: 'Mantas' };

        client
            .post('api/greeting', data)
            .then(function(greeting) {
                expect(greeting.text).toBe('XXX');
                done();
            });
    });

    it("handle a 404", function(done) {
        client
            .get('xyz.html')
            .catch(function(error) {
                expect(error).toBe('Not Found');
                done();
            });
    });

    it("handle a connection error", function(done) {
        new HttpClient('http://xyzdef:65535/')
            .get('xyz.html')
            .catch(function(error) {
                expect(error).toBe('Connection refused');
                done();
            });
    });

    it("be a failed try", function() {
        expect(true).toBe(false);
    });
});