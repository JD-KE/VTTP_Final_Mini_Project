import { Injectable, inject } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Tokens, User, UserLogin, UserRegister } from './model';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private http = inject(HttpClient)
  private router = inject(Router)
  private jwtHelper = inject(JwtHelperService)

  isLoggedin:boolean = false;

  user!:String

  constructor() { }
  
  setJwt(tokens:Tokens) {
    localStorage.setItem("eh_a_token",tokens.access_token)
    localStorage.setItem("eh_r_token", tokens.refresh_token)
  }

  logoff() {

    localStorage.removeItem("eh_a_token")
    this.isLoggedin = false

    const url = environment.backendBaseUrl + "/auth/logout"
    this.http.get(url).subscribe().unsubscribe()
    
  }

  logBackIn() {
    const token = localStorage.getItem("eh_a_token")
    console.log('testing logBackIn')
    if(!!token) {
      const decoded = this.jwtHelper.decodeToken(token)
      this.user = decoded.sub
    } 

    
  }

  login(loginDetails:UserLogin) {
    const url = environment.backendBaseUrl + "/auth/authenticate"
    this.http.post<any>(url, loginDetails).subscribe({
      next: data => {
        console.log(data)
        this.setJwt(data)
        console.log(localStorage.getItem("eh_a_token"))
        console.log(localStorage.getItem("eh_r_token"))
        this.user = loginDetails.username
      },

    })
  }

  register(registerDetails:UserRegister) {
    const url = environment.backendBaseUrl + "/auth/register"
    this.http.post<any>(url, registerDetails).subscribe({
      next: data => {
        console.log(data)
        this.setJwt(data)
        console.log(localStorage.getItem("eh_a_token"))
        console.log(localStorage.getItem("eh_r_token"))
        this.user = registerDetails.username
      },

    })
  }





}
