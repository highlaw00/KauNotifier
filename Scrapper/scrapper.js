const puppeteer = require("puppeteer");

const BASE_URL = "http://kau.ac.kr/web/pages";
const SUFFIX = ".do";
const TITLE_SELECTOR_SUFFIX = " tr td a";
const WRITER_SELECTOR_SUFFIX = " .not_m";

class SchoolInfo {
  constructor() {
    this.index = "/gc14561b";
    this.url = BASE_URL + this.index + SUFFIX;
    this.baseSelector = "#notiDfTable";
    this.titleSelector = this.baseSelector + TITLE_SELECTOR_SUFFIX;
    this.writerSelector = this.baseSelector + WRITER_SELECTOR_SUFFIX;
  }
}

class NormalInfo {
  constructor() {
    this.index = "/gc32172b";
    this.url = BASE_URL + this.index + SUFFIX;
    this.baseSelector = "#bbsDfTable";
    this.titleSelector = this.baseSelector + TITLE_SELECTOR_SUFFIX;
    this.writerSelector = this.baseSelector + WRITER_SELECTOR_SUFFIX;
  }
}

const schoolInfo = new SchoolInfo();
const normalInfo = new NormalInfo();

console.log(schoolInfo.titleSelector);
console.log(normalInfo.titleSelector);
const targetInfos = [schoolInfo, normalInfo];

const test = async (targetInfo) => {
  const { url, titleSelector, writerSelector } = targetInfo;

  const browser = await puppeteer.launch({ headless: "new" });
  const page = await browser.newPage();

  await page.goto(url);
  await page.waitForSelector(".emp");

  const notices = await page.evaluate(
    (titleSelector, writerSelector) => {
      console.log(titleSelector, writerSelector);
      const titleElements = document.querySelectorAll(titleSelector);
      const titleArray = Array.from(titleElements).map((element) => ({
        title: element.innerText,
      }));
      const writerElements = document.querySelectorAll(writerSelector);
      const writerArray = Array.from(writerElements)
        .filter((element) => {
          if (element.innerText === "") return false;
          else return true;
        })
        .map((element) => element.innerText);

      const objArray = [];
      for (let i = 0; i < writerArray.length; i += 3) {
        const newObj = {};
        for (let j = 0; j < 2; j += 1) {
          if (j === 0) {
            newObj.writer = writerArray[i + j];
          } else {
            newObj.date = writerArray[i + j];
          }
        }
        objArray.push(newObj);
      }

      const mergedArray = titleArray.map((title, idx) => ({
        ...title,
        ...objArray[idx],
      }));
      return mergedArray;
    },
    titleSelector,
    writerSelector
  );

  notices.forEach((obj) => console.log(obj));

  await browser.close();
};

// const test_school = async () => {
//   const browser = await puppeteer.launch({ headless: "new" });
//   const page = await browser.newPage();

//   await page.goto(SCHOOL_NOTI_URL);
//   await page.waitForSelector(".emp");

//   const notices = await page.evaluate(() => {
//     const titleElements = document.querySelectorAll("#notiDfTable tr td a");
//     const titleArray = Array.from(titleElements).map((element) => ({
//       title: element.innerText,
//     }));
//     const writerElements = document.querySelectorAll("#notiDfTable .not_m");
//     const writerArray = Array.from(writerElements)
//       .filter((element) => {
//         if (element.innerText === "") return false;
//         else return true;
//       })
//       .map((element) => element.innerText);

//     const objArray = [];
//     for (let i = 0; i < writerArray.length; i += 3) {
//       const newObj = {};
//       for (let j = 0; j < 2; j += 1) {
//         if (j === 0) {
//           newObj.writer = writerArray[i + j];
//         } else {
//           newObj.date = writerArray[i + j];
//         }
//       }
//       objArray.push(newObj);
//     }

//     const mergedArray = titleArray.map((title, idx) => ({
//       ...title,
//       ...objArray[idx],
//     }));
//     return mergedArray;
//   });

//   notices.forEach((obj) => console.log(obj));

//   await browser.close();
// };
for (const obj of targetInfos) {
  test(obj);
}
