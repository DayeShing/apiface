import Index from ".././layout/index.vue";
import Page from ".././layout/layout.vue";
export default {
  routes: [{
    path: "/",
    components: {
      content: Index
    }
  }, {
    path: "/page",
    components: {
      content: Page
    }
  }]
};
