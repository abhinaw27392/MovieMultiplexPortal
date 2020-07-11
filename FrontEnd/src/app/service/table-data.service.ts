import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
const API_URL = "http://localhost:3000/data";
@Injectable({
  providedIn: 'root'
})
export class TableDataService {

  constructor(private http: HttpClient) { }
  getallData(){
    return this.http.get(API_URL);
  }

}
