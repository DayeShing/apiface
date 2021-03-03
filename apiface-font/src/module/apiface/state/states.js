export default {
  states: {
    state: {
      userState:{
        userName: ''
      },
      
    },

    mutations: {
      updateUserStates: (state, userName) => { //vuex更新用户状
        state.userState.userName = userName;
      },
      clearUserStates: (state) => { //清除用户状态
        state.userState = {
          account: '',
          userName: ''
        }
      }
    },
    getters: {
      getUserState: state => {
        return state.userState;
      }
    }
  },
}
