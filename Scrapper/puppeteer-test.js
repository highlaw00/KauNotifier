const puppeteer = require('puppeteer');
const url = 'https://kau.ac.kr/web/pages/gc32172b.do';

(async () => {
    const browser = await puppeteer.launch();
    const page = await browser.newPage();
    await page.goto(url);
    const hrefElement = await page.$('.tit a');
    await hrefElement.click();
})