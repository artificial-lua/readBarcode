const express = require('express')
const app = express()

app.use('/', express.static('css'));
app.use('/', express.static('js'));
app.use('/', express.static('icon'));

app.get('/barcode/req/', function(req, res){
	var respond = req.query;
    console.log(respond);
	const ip = req.headers['x-forwarded-for'] ||  req.connection.remoteAddress;
	var date = new Date();
	res.send(respond['barcode']);
	console.log('[' + date + '] Index.js Respond to ' + ip);
})


// var vue = require('./routes/Vue.js')(app);
// app.use('/vue.js', vue);


// var guest_book = require('./routes/guest_book.js')(app);
// app.use('/guest_book', guest_book);


// var work = require('./routes/work.js')(app);
// app.use('/work', work);


app.listen(port, function(){
	console.log('Example app listening on port ' + port + '!');
	})
