import { Injectable, inject } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Tokens, User, UserLogin, UserRegister } from './model';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserStore } from './user.store';
import { lastValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private http = inject(HttpClient)
  private router = inject(Router)
  private jwtHelper = inject(JwtHelperService)
  private userStore = inject(UserStore)


  isLoggedin:boolean = false;

  user!:string

  constructor() { }

  logout() {

    const url = environment.backendBaseUrl + "/auth/logout"

    lastValueFrom(this.http.post(url,''))
      .then(value => {
        localStorage.removeItem("eh_a_token")
        localStorage.removeItem("eh_r_token")
        this.isLoggedin = false
        this.userStore.removeUser()
      })
  }

  logBackIn() {
    const token = localStorage.getItem("eh_a_token")
    console.log('testing logBackIn')
    console.log(!!token)
    if(!!token) {
      const decoded = this.jwtHelper.decodeToken(token)
      
      const expired = this.jwtHelper.isTokenExpired(token)
      if (!!!expired) {
        console.log('access token not expired')
        this.userStore.addUser(decoded.sub)
        return
      }
    } 

    this.refreshToken()    
  }

  login(loginDetails:UserLogin) {
    const url = environment.backendBaseUrl + "/auth/authenticate"
    lastValueFrom(this.http.post<any>(url, loginDetails))
      .then(value => {
        console.log(value)
        this.setJwt(value)
        console.log(localStorage.getItem("eh_a_token"))
        console.log(localStorage.getItem("eh_r_token"))
        const token = localStorage.getItem("eh_a_token")
        if(!!token) {
          this.userStore.addUser(this.jwtHelper.decodeToken(token).sub)
        }
      })
      .catch(error => {
        alert("Invalid login")
      })

  }

  register(registerDetails:UserRegister) {
    const url = environment.backendBaseUrl + "/auth/register"

    lastValueFrom(this.http.post<any>(url, registerDetails))
      .then(value => {
        console.log(value)
        this.setJwt(value)
        console.log(localStorage.getItem("eh_a_token"))
        console.log(localStorage.getItem("eh_r_token"))
        const token = localStorage.getItem("eh_a_token")
        if(!!token) {
          this.userStore.addUser(this.jwtHelper.decodeToken(token).sub)
        }
      })
      .catch(error => {
        alert(error.error.message)
      })

  }

  private setJwt(tokens:Tokens) {
    localStorage.setItem("eh_a_token",tokens.access_token)
    localStorage.setItem("eh_r_token", tokens.refresh_token)
  }

  refreshToken() {
    const refreshToken = localStorage.getItem("eh_r_token")
    console.log('refresh token')
    if(!!refreshToken) {
      if(this.jwtHelper.isTokenExpired(refreshToken)) {
        this.logout()
        this.router.navigate([''])
      } else {
        const url = environment.backendBaseUrl + "/auth/refresh-token"
        // this.http.post<any>(url,'').subscribe({
        //   next: data => {
        //     this.setJwt(data)
        //     const token = localStorage.getItem("eh_a_token")
        //     if(!!token) {
        //       this.userStore.addUser(this.jwtHelper.decodeToken(token).sub)
        //     }
        //   },
        //   error: error => {
        //     alert('There was problem with the authentication. Please log back in')
        //     this.router.navigate(['/login'])
        //   }
        // })

        lastValueFrom(this.http.post<any>(url,''))
          .then(value => {
            this.setJwt(value)
            const token = localStorage.getItem("eh_a_token")
            if(!!token) {
              this.userStore.addUser(this.jwtHelper.decodeToken(token).sub)
            }
          })
          .catch(error => {
            alert('There was problem with the authentication. Please log back in')
            this.router.navigate(['/login'])
          })
      }
    }
  }

}
