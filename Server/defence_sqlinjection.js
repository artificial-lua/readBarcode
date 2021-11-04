default_injection_keywords = [
    "'",
    "\"",
    ";",
    "--",
    "\\",
    "\\'",
    "\\\"",
    "\\;",
    "\\--",
    '"',
    "`"
]

mode1_injection_keywords = [
    "select",
    "insert",
    "update",
    "delete",
    "drop",
    "create",
    "alter",
    "truncate",
    "rename",
    "grant",
    "revoke"
]

exports.default_check = function(str) {
    return check_injection(str, default_injection_keywords);
}

exports.check_mode1 = function(str) {
    return (check_injection(str, default_injection_keywords) && check_injection(str, mode1_injection_keywords));
}

exports.custom_check = function(str, injection_keywords) {
    return check_injection(str, injection_keywords);
}


// function, search injection keywords in string function, and return true if found
function check_injection(str, injection_keywords) {
    for (let i in injection_keywords) {
        if (str.indexOf(injection_keywords[i]) != -1) {
            return true;
        }
    }
    return false;
}