describe("a greeting service", function() {
    it("be a successful try", function() {
        expect(true).to.be.true;
    });

    it("be a successful promise result", function() {
        return Promise.resolve(true);
    });

    it("be a rejected promise result", function() {
        return Promise.reject('Error');
    });

    it("be a failed try", function() {
        expect(true).to.be.false;
    });

    xit("be ignored", function() {
        expect(true).to.be.false;
    });

    it("be a successful try with deferred done", function(done) {
        setTimeout(function() {
            expect(true).to.be.true;
            done();
        })
    });
});