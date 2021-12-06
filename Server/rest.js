const XMLHttpRequest = require("xhr2");

class Crawler{
    constructor(foodsafety = {
        url : "http://openapi.foodsafetykorea.go.kr/api/",
        keyId : "f65590d907254730a315",
        serviceId : "C005/json/1/5/BAR_CD="
    }){
        this.url = [];
        this.xhr = new XMLHttpRequest();
        this.get = []

        this.url.push("https://www.beepscan.com/barcode/");

        this.url.push(foodsafety.url + foodsafety.keyId + "/" + foodsafety.serviceId)

        this.get.push(async (barcode) => {

            const str = await this.rest(this.url[0] + barcode, "GET").then();
            
            // const title to match str between <b> and </b>
            if(str != null){
                let title = str.match(/<b>(.*)<\/b>/);
                if(title != null){
                    return title[1];
                }
            }else{
                return null;
            }
        });

        this.get.push(async (barcode) => {
            const result = JSON.parse(await this.rest(this.url[1] + barcode, "GET").then());
            if(result.C005.RESULT.CODE == 'INFO-200'){
                return null;
            }else{
                return result.C005.row.pop().PRDLST_NM;
            }
        });
    }
    
    async request(barcode, url_num){
        return await this.get[url_num](barcode)
    }

    async rest(url, method="GET"){
        return new Promise((resolve, reject) => {
            let xhr = this.xhr;
            console.log("request : " + url);
            xhr.onload = function(){
                xhr.status===200?resolve(xhr.responseText):reject(xhr.statusText);
            }
        
            // open() 메서드는 요청을 준비하는 메서드입니다. (http 메서드, 데이터를 받아올 URL 경로, 비동기 여부)
            xhr.open(method, url, true);
        
            xhr.send("");
        });
    }
}

exports.Crawler = Crawler;

async function main(){
    barcode = "8801115111030";
    
    let crawler = new Crawler();
    let result = await crawler.request(barcode, 0).then();
    console.log(result);
} 

// main();