const LOCAL_URL = 'http://localhost:8080'
const PRODUCTION_URL = 'http://kau-notifier.site'
const BASE_URL = LOCAL_URL

export const API = {
    MAIN: `${BASE_URL}`,
    SEND_MAIL: `${BASE_URL}/verify-request`,
    VERIFY: `${BASE_URL}/verify`,
    SOURCES: `${BASE_URL}/sources`,
    SUBSCRIBE: `${BASE_URL}/subscribe`
}