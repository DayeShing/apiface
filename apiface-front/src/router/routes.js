const Index = () => import('layout/index.vue')
const Layout = () => import('layout/layout.vue')

const main = "content";
const routes = [{
    path: '/',
    redirect: '/index'
  }, {
    path: "/index",
    components: {
      [main]: Index
    }
  },
  {
    path: "/inner",
    components: {
      [main]: Layout
    }
  }
]
export default routes