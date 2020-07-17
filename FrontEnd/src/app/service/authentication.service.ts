import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { USER_API_URL } from './../constant/constant';
import { Router } from '@angular/router';
import { Login } from './../model/login';
import { AlertService } from './alert.service';
import { map } from 'rxjs/operators';
import { UserDetail } from './../model/userDetail';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  userId: string;
  constructor(private http: HttpClient, private alertService: AlertService, private router: Router) {
  }

  authenticate(login: Login) {
    //create a token for authentication request
    let authToken = "Basic " + window.btoa(login.username + ":" + login.password);

    //header customization
    let headers = new HttpHeaders({
      Authorization: authToken
    });

    return this.http.get(USER_API_URL + "/login", { headers }).pipe(
      map(successData => {
        sessionStorage.setItem("token", authToken);
        sessionStorage.setItem('user', login.username);
        return successData;
      })
    ).subscribe((res: UserDetail) => {
      this.router.navigate(['/home']);
      this.userId = res.id;
    }, err => {
      if (err.status == 401) {
        this.alertService.error("Invalid Username or password!", true);
      } else {
        this.alertService.error(err.error.message, true);
      }
    }
    );
  }

  getAuthenticationToken() {
    return sessionStorage.getItem("token");
  }

  isUserLoggedIn(): boolean {
    let user = sessionStorage.getItem('user');
    if (user == null) {
      return false;
    } else {
      return true;
    }
  }

  logOut() {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('user');
    this.userId = null;
    this.router.navigate(['/login']);
    this.alertService.success("You are Successfully Logged out!", true);
  }

}
