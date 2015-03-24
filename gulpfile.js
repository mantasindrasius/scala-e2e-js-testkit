/*var gulp = require('gulp');
var webserver = require('gulp-webserver');

gulp.task('webserver', function() {
    gulp.src('app')
        .pipe(webserver({
            livereload: true,
            directoryListing: true,
            open: true
        }));
});*/

var gulp = require('gulp'),
    //less = require('gulp-less'),
    express = require('express'),
    ecstatic = require('ecstatic'),
    http = require('http');

var io = require('socket.io');

gulp.task('present', function() {
    var app = express();
    app.use(ecstatic({ root: __dirname + '/present' }));

    var server = http.createServer(app).listen(3000, '0.0.0.0');
    var servIO = io.listen(server);

    servIO.sockets.on('connection', function (socket) {
        console.log('Client connected', socket.id, socket.request.connection.remoteAddress);

        socket.emit("message", "Welcome to Revealer");

        socket.on('slidechanged', function(data){
            socket.broadcast.emit('slidechanged', data);
        });
    });
});
