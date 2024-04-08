import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../environments/environment';
import { EventModel } from './model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private http:HttpClient = inject(HttpClient);

  constructor() { }

  baseUrl:string = environment.backendBaseUrl;

  createEvent(event:EventModel):Observable<any> {
    const url:string = this.baseUrl + "/event/create"

    return this.http.post<any>(url, event)
  }
  
  updateEvent(event:EventModel):Observable<any> {
    const url:string = this.baseUrl + "/event/update"

    return this.http.put<any>(url, event)
  }

  getUserEvents():Observable<EventModel[]> {
    const url = this.baseUrl + "/event/user"

    return this.http.get<EventModel[]>(url)
  }

  getEventById(id:string):Observable<EventModel> {
    const url = this.baseUrl + `/event/view/${id}`
    return this.http.get<EventModel>(url)
  }
}
