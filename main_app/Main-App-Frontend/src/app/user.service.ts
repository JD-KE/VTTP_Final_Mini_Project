import { Injectable, inject } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Tokens, User, UserLogin, UserRegister } from './model';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserStore } from './user.store';
import { Observable, lastValueFrom } from 'rxjs';
import { LoadingOverlayRef, LoadingService } from './loading.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private http = inject(HttpClient)
  private router = inject(Router)
  private jwtHelper = inject(JwtHelperService)
  private userStore = inject(UserStore)
  private loadingSvc = inject(LoadingService)

  isLoggedin:boolean = false
  user!:string

  loadingRef!:LoadingOverlayRef

  constructor() { }

  logout() {

    const url = environment.backendBaseUrl + "/auth/logout"
    this.loadingRef = this.loadingSvc.open()

    lastValueFrom(this.http.post(url,''))
      .then(value => {
        localStorage.removeItem("accessToken")
        localStorage.removeItem("refreshToken")
        this.isLoggedin = false
        this.userStore.removeUser()
        this.loadingRef.close()
        this.router.navigate([this.router.url])
      })
      .catch(error => {
        localStorage.removeItem("accessToken")
        localStorage.removeItem("refreshToken")
        this.isLoggedin = false
        this.userStore.removeUser()
        this.loadingRef.close()
        this.router.navigate([this.router.url])
      })
  }

  logBackIn() {
    const token = localStorage.getItem("accessToken")
    // console.log('testing logBackIn')
    // console.log(!!token)
    if(!!token) {
      const decoded = this.jwtHelper.decodeToken(token)
      
      const expired = this.jwtHelper.isTokenExpired(token)
      if (!!!expired) {
        // console.log('access token not expired')
        this.userStore.addUser(decoded.sub)
        return
      }
    } 

    this.refreshToken()    
  }

  login(loginDetails:UserLogin) {
    const url = environment.backendBaseUrl + "/auth/authenticate"
    this.loadingRef = this.loadingSvc.open()

    lastValueFrom(this.http.post<any>(url, loginDetails))
      .then(value => {
        // console.log(value)
        this.setJwt(value)
        // console.log(localStorage.getItem("accessToken"))
        // console.log(localStorage.getItem("refreshToken"))
        const token = localStorage.getItem("accessToken")
        if(!!token) {
          this.userStore.addUser(this.jwtHelper.decodeToken(token).sub)
        }
        this.loadingRef.close()
        this.router.navigate(['/'])
      })
      .catch(error => {
        this.loadingRef.close()
        alert("Invalid login")
      })

  }

  register(registerDetails:UserRegister) {
    const url = environment.backendBaseUrl + "/auth/register"
    this.loadingRef = this.loadingSvc.open()

    lastValueFrom(this.http.post<any>(url, registerDetails))
      .then(value => {
        // console.log(value)
        this.setJwt(value)
        // console.log(localStorage.getItem("accessToken"))
        // console.log(localStorage.getItem("refreshToken"))
        const token = localStorage.getItem("accessToken")
        if(!!token) {
          this.userStore.addUser(this.jwtHelper.decodeToken(token).sub)
        }
        this.loadingRef.close()
        this.router.navigate(['/'])
      })
      .catch(error => {
        this.loadingRef.close()
        alert(error.error.message)
      })

  }

  private setJwt(tokens:Tokens) {
    localStorage.setItem("accessToken",tokens.access_token)
    localStorage.setItem("refreshToken", tokens.refresh_token)
  }

  refreshToken() {
    const refreshToken = localStorage.getItem("refreshToken")

    // console.log('refresh token')
    if(!!refreshToken) {
      if(this.jwtHelper.isTokenExpired(refreshToken)) {
        this.logout()
        this.router.navigate([''])
      } else {
        this.loadingRef = this.loadingSvc.open()
        const url = environment.backendBaseUrl + "/auth/refresh-token"

        lastValueFrom(this.http.post<any>(url,''))
          .then(value => {
            this.setJwt(value)
            const token = localStorage.getItem("accessToken")
            if(!!token) {
              this.userStore.addUser(this.jwtHelper.decodeToken(token).sub)
            }
            this.loadingRef.close()
          })
          .catch(error => {
            this.logout()
            this.loadingRef.close()
            alert('There was a problem with the authentication. Please log back in.')
            this.router.navigate(['/login'])
          })
      }
    }
  }

  checkifUsernameExists(username:string):Observable<boolean> {
    const url = environment.backendBaseUrl + "/user/check/username"

    return this.http.post<boolean>(url,username)
  }

  checkifEmailExists(email:string):Observable<boolean> {
    const url = environment.backendBaseUrl + "/user/check/email"

    return this.http.post<boolean>(url,email)
  }

}
