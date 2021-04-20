import Cookie from 'js-cookie'
import VueI18n from 'vue-i18n'
import {
  zh_cn
} from "./zh_cn.js";
import {
  ja
} from "./ja.js";
import {
  zh_hk
} from "./zh_hk.js";
import {
  en
} from "./en.js";
export default {
  setLang(lang) {
    Cookie.set('self_lang', lang);
  },
  getLang(defaultLang) {
    let localLang = Cookie.get('self_lang');
    if (localLang) {
      return localLang;
    } else {
      if (defaultLang) {
        return defaultLang;
      } else {
        var lang = (navigator.language || navigator.browserLanguage).toLowerCase();
        if (lang.indexOf('zh') >= 0) {
          return 'zh_cn';
        } else {
          return 'en';
        }
      }
    }
  },
  i18nInit() {
    const messages = {
      "en": en,
      "zh_cn": zh_cn,
      "zh_hk": zh_hk,
      "ja": ja
    };
    return new VueI18n({
      locale: this.getLang('zh_cn'),
      messages
    });
  }
}
