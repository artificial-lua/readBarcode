const fs = require('fs');

exports.open = function(string)
{
    const jsonFile = fs.readFileSync(string, 'utf8');

    config = JSON.parse(jsonFile);

    return config
}