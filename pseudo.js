import { dom } from 'goog.dom';
import { events } from 'goog.events';
import { XhrIo as xhr } from 'goog.net.Xhr';
import { go, take, put, Queue} from 'core.async';

const YT_SEARCH_URL= "https://www.googleapis.com/youtube/v3/search?key=AIzaSyDqwW1i0Ul8s9yN6nKD5MAhnCCSTzOJDyg&part=snippet&q=";

function listen(el, type) {
  let out = new Queue();
  events.listen(el, type, (e => {
    put(out, e);
  }))

  return out;
}

function json(uri) {
  let out = new Queue();

  xhr.send(uri, (e) => {
    var items = e.target.getResponseJson().items;
    titles = items.map(item => { return item.snippet.title });
    put(out, titles);
  })

  return out;
}

const queryUrl = q => {
  return `YT_SEARCH_URL${q}`
};

const userQuery = () => {
  return dom.getElement("query").value;
};

const renderQuery = results => {
  let items = results.map(result => {
    return `<li>${result}</li>`;
  }).join("");

  return `<ul>${items}</ul>`;
}

function init() {
  let clicks = listen(dom.getElement("search"), "click");
  let resultsView = dom.getElement("results");

  go(() => {
    while (true) {
      take(clicks);
      let results = take(json(queryUrl(), userQuery()));

      if (results) {
        resultsView.innerHTML = renderQuery(results);
      }
    };
  });
}

init();








