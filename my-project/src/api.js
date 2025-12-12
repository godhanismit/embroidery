import axios from 'axios';

const API = axios.create({
  // eslint-disable-next-line no-undef
  baseURL: 'http://localhost:8080/api'
});

export default API;
