import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Multiplex } from 'src/app/model/multiplex';
import { MULTIPLEX_API_URL } from './../../constant/constant';

@Injectable({
  providedIn: 'root'
})
export class MultiplexDataService {

  constructor(private http: HttpClient) { }

  getallData() {
    return this.http.get(MULTIPLEX_API_URL);
  }

  getOneMultiplex(id: string) {
    return this.http.get(MULTIPLEX_API_URL + "/" + id);
  }

  addMultiplex(multiplex: Multiplex, userId: string) {
    return this.http.post(MULTIPLEX_API_URL + "?userId=" + userId, multiplex);
  }

  updateMultiplex(id: string, multiplex: Multiplex, userId: string) {
    return this.http.put(MULTIPLEX_API_URL + "/" + id + "?userId=" + userId, multiplex);
  }

  deleteMultiplex(id: string, userId: string) {
    return this.http.delete(MULTIPLEX_API_URL + "/" + id + "?userId=" + userId);
  }
}
