const Index = () => import('layout/index.vue')
const Layout = () => import('layout/layout.vue')
const Auth = () => import('layout/auth.vue')

const PostMan = () => import('page/postman/index.vue')
const main = "content";
const routes = [{
  path: '/',
  redirect: '/index'
}, {
  path: "/index",
  name: "index",
  components: {
    [main]: Index
  }
}, {
  path: "/index/:type/:id",
  name: "face",
  components: {
    [main]: Index
  }
}, {
  path: "/inner",
  components: {
    [main]: Layout
  }
}, {
  path: "/auth/:type",
  name: "auth",
  components: {
    [main]: Auth
  }
}, {
  path: "/postman",
  components: {
    [main]: PostMan
  }
}]
export default routes