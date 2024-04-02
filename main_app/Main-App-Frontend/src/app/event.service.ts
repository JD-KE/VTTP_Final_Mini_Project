import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../environments/environment';
import { EventBooking } from './model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private http:HttpClient = inject(HttpClient);

  constructor() { }

  baseUrl:string = environment.backendBaseUrl;

  createEvent(event:EventBooking):Observable<any> {
    const url:string = this.baseUrl + "/event/create"

    return this.http.post<any>(url, event)
  }
}
