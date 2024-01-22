require("dotenv").config();
const mysql = require("mysql2/promise");
const puppeteer = require("puppeteer");

async function getSources() {
  try {
    const connection = await mysql.createConnection(process.env.DATABASE_URL);
    const [rows, fields] = await connection.query("select * from source");
    connection.end();
    return rows;
  } catch (error) {
    console.log("Error while DB Connections.");
    console.log(error);
  }
}

async function scrapeData(url) {
  console.log("Target URL: ", url);

  const browser = await puppeteer.launch({ headless: "new" });

  try {
    const page = await browser.newPage();

    await page.goto(url);

    // wait for client-side loading
    await page.waitForSelector(".tit");

    // get texts from html. ignore this code.
    const titles = await page.$$eval(".tit a", (elements) => {
      return elements.map((element) => element.innerText);
    });

    const dates = await page.$$eval(".tit a .post_info .date", (elements) => {
      return elements.map((element) => element.innerText);
    });

    const urls = [];

    const links = await page.$$(".tit a");
    const linksLen = links.length;
    for (let i = 0; i < linksLen; i++) {
      // n번째 공지사항 요소 가져오기
      const tempLink = await page.$$(".tit a");
      const currLink = tempLink[i];

      // n번째 공지사항 요소 클릭 후 대기
      await Promise.all([
        page.waitForNavigation(),
        currLink.evaluate((el) => el.click()),
      ]);

      // url 저장 후 이전으로 돌아오기
      urls.push(page.url());
      await Promise.all([page.waitForSelector(".tit"), page.goBack()]);
    }

    const fetchedUrls = urls;

    const result = titles.map((title, idx) => {
      const url = fetchedUrls[idx];
      const date = dates[idx];
      return {
        title: title,
        date: date,
        url: url,
      };
    });

    return result;
  } finally {
    await browser.close();
  }
}

// const sourceRows = getSources().then((result) => {
//   result.forEach((row) => {
//     const targetUrl = row.url;
//     const targetDescription = row.description;
//     scrapeData(targetUrl).then((result) => {
//       console.log(targetDescription);
//       console.log(result);
//     });
//   });
// });
// console.log(sourceRows);

// const targetUrl = "https://kau.ac.kr/web/pages/gc14561b.do";
// scrapeData(targetUrl)
//   .then((result) => {
//     // console.log("Scraped Titles:", result.titles);
//     // console.log("Scraped Dates:", result.dates);
//     // console.log("New URL after click:", result.fetchedUrls);
//     console.log(result);
//   })
//   .catch((error) => console.error("Error during scraping:", error));

const solution = async () => {
  const start = Date.now();
  for (const row of await getSources()) {
    const url = row.url;
    const description = row.description;
    const result = await scrapeData(url);

    console.log(description);
    console.log(result);
  }
  const end = Date.now();

  console.log("Process time:", end - start);
};

solution();
