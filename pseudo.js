import { dom } from 'goog.dom';
import { events } from 'goog.events';
import { go, take, put, chan} from 'core.async';


function listen(el, type) {
  let out = chan();
  events.listen(el, type, (e => {
    put(out, e);
  }));

  return out;
}

let clicks = listen(dom.getElement("search"), "click");

go(() => {
  while (true) {
    let e = take(clicks);
    console.log(e);
  };
});








