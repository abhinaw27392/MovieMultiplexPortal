import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import {SignUp} from './../model/signup';
import {USER_API_URL} from './../constant/constant';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private http: HttpClient) { }

  register(signUp: SignUp) {
    return this.http.post(USER_API_URL, signUp);
  }
}
