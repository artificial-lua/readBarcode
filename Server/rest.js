const XMLHttpRequest = require("xhr2");

async function rest(url, method="GET"){
    return new Promise((resolve, reject) => {
        let xhr = new XMLHttpRequest(); // ajax
        console.log("request : " + url);
        xhr.onload = function(){
            xhr.status===200?resolve(xhr.responseText):reject(xhr.statusText);
        }
    
        // open() 메서드는 요청을 준비하는 메서드입니다. (http 메서드, 데이터를 받아올 URL 경로, 비동기 여부)
        xhr.open(method, url, true);
    
        xhr.send("");
    });
}

function getTitle(str){
    // const title to match str between <b> and </b>
    if(str != null){
        let title = str.match(/<b>(.*)<\/b>/);
        if(title != null){
            return title[1];
        }
    }else{
        return null;
    }
}


//function crawling
function crawling(barcode, url){
    const result = rest(url + barcode, "GET").then();
    return result;
}

async function crawling_beepscan(barcode){
    return getTitle(await crawling(barcode, "https://www.beepscan.com/barcode/"));
}

exports.crawling_beepscan = crawling_beepscan;

async function crawling_foodsafetykorea(barcode){
    // const result = JSON.parse(await crawling(barcode, ));
    // if(result.C005.RESULT.CODE == 'INFO-200'){
    //     return null;
    // }else{
    //     return result.C005.row.pop().PRDLST_NM;
    // }
    return "testing code";
}

exports.crawling_foodsafetykorea = crawling_foodsafetykorea;

async function main(){
    barcode = "8801115111030";

    // console.log(await crawling_foodsafetykorea(barcode));
    console.log(await crawling_beepscan(barcode));
}

main();