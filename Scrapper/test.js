require('dotenv').config();
const mysql = require("mysql2/promise");
const puppeteer = require('puppeteer');

const getSources = async () => {
    try {
        const connection = await mysql.createConnection(process.env.DATABASE_URL);
        const [rows, fields] = await connection.query("select * from source");
        connection.end();
        return rows;
    } catch (error) {
        console.log("Error!");
        console.log(error);
    }
}

const scrap = async (sources) => {

    // 스크랩할 URL와 이름 추출
    const targets = []
    sources.forEach((element) => {
        obj = {name: element.name, url: element.url};
        targets.push(obj);
    })

    // 가상 브라우저 실행
    const browser = await puppeteer.launch({ headless: "new" });
    const page = await browser.newPage();

    // 공지사항 출처를 iteration하며 동적 스크랩
    for (const target of targets) {
        url = target.url;
        targetName = target.name;
        console.log(targetName);

        await page.goto(url);
        await page.waitForSelector('.tit');

        const notices = await page.evaluate(
            () => {
                const titleElements = document.querySelectorAll('.tit a');
                const dateElements = document.querySelectorAll('.tit a .date');
                const titles = Array.from(titleElements).map((elem) => elem.innerText);
                const dates = Array.from(dateElements).map((elem) => elem.innerText);
                const results = titles.map((title, idx) => {
                    date = dates[idx];
                    return {
                        title: title,
                        date: date,
                    }
                })
                return results;
            }
        )
        
        const titleHandlers = await page.$$('.tit');
        console.log(titleHandlers[0]);
        const urls = await getUrls(page, titleHandlers);
        console.log(urls);
    }

    browser.close();
};

const getUrls = async (page, handlers) => {
    const urls = []
    console.log("GetURLS init");
    for (const handler of handlers) {
        console.log(handler.innerText);

        await handler.click();
        console.log("clicked");
    
        // 클릭 후 페이지 로딩 기다리기
        await page.waitForTimeout(10000);
        // await page.waitForNavigation({ waitUntil: 'domcontentloaded' });
        // await page.waitForSelector('.sub_article');
        console.log("loaded.");
    
        // 현재 페이지의 URL 얻기
        const url = page.url();
        console.log(url);
    
        urls.push(url);
    
        // 이전 페이지로 돌아가기
        await page.goBack();

        // 이전 페이지로 돌아가기 전에 페이지 로딩 기다리기
        await page.waitForNavigation({ waitUntil: 'domcontentloaded' });
    }
    return urls;
}

const solution = async () => {
    sources = await getSources();
    scrap(sources);
}

solution();