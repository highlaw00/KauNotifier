const LOCAL_URL = 'http://localhost:8080/api'
const PRODUCTION_URL = 'http://3.37.154.212/api'
const BASE_URL = PRODUCTION_URL

export const API = {
    MAIN: `${BASE_URL}`,
    SEND_MAIL: `${BASE_URL}/verify-request`,
    VERIFY: `${BASE_URL}/verify`,
    SOURCES: `${BASE_URL}/sources`,
    SUBSCRIBE: `${BASE_URL}/subscribe`,
    FIND: `${BASE_URL}/find`,
    SUBSCRIPTIONS: `${BASE_URL}/subscriptions`
}