const puppeteer = require("puppeteer");

const BASE_URL = "http://kau.ac.kr/web/pages";
const SUFFIX = ".do";
const SCHOOL_NOTI_INDEX = "/gc14561b";
const SCHOOL_NOTI_URL = BASE_URL + SCHOOL_NOTI_INDEX + SUFFIX;

const test = async () => {
  const browser = await puppeteer.launch({ headless: "new" });
  const page = await browser.newPage();

  console.log("Browser Init");

  await page.goto(SCHOOL_NOTI_URL);
  await page.waitForSelector(".emp");

  const titles = await page.evaluate(() => {
    const titleElements = document.querySelectorAll("#notiDfTable tr td a");
    const titleArray = Array.from(titleElements).map(
      (element) => element.innerText
    );
    return titleArray;
  });

  const writers = await page.evaluate(() => {
    const writerElements = document.querySelectorAll("#notiDfTable .not_m");
    const writerArray = Array.from(writerElements)
      .filter((element) => {
        if (element.innerText === "") return false;
        else return true;
      })
      .map((element) => element.innerText);

    const objArray = [];
    for (let i = 0; i < writerArray.length; i += 3) {
      const new_obj = {};
      for (let j = 0; j < 2; j += 1) {
        if (j === 0) {
          new_obj.writer = writerArray[i + j];
        } else {
          new_obj.date = writerArray[i + j];
        }
      }
      objArray.push(new_obj);
    }

    return objArray;
  });

  const notices = await page.evaluate(() => {
    const titleElements = document.querySelectorAll("#notiDfTable tr td a");
    const writerElements = document.querySelectorAll("#notiDfTable .not_m");
    const titleArray = Array.from(titleElements).map(
      (element) => element.innerText
    );
    const writerArray = Array.from(writerElements).map(
      (element) => element.innerText
    );
  });

  titles.forEach((title) => console.log(title));
  writers.forEach((writer) => console.log(writer));

  await browser.close();
};

test();
