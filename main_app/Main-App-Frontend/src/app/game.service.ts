import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { environment } from '../environments/environment';
import { Game, GameResults } from './model';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  private http:HttpClient = inject(HttpClient);

  constructor() { }

  baseUrl:string = environment.backendBaseUrl;

  getGamesResults(searchTerm:string, page:number, limit:number):Observable<GameResults> {
    const url:string = this.baseUrl + "/game/search"
    const params = new HttpParams()
      .set("searchTerm", searchTerm)
      .set("page", page)
      .set("limit", limit);
    return this.http.get<GameResults>(url, {params:params})
  }

  getGamesResultsPromise(searchTerm:string, page:number, limit:number):Promise<GameResults> {
    return lastValueFrom(this.getGamesResults(searchTerm, page, limit));
  }

  getGameById(id:number):Observable<Game> {
    const url:string = this.baseUrl +`/game/${id}`
    return this.http.get<Game>(url)
  }

  getGameByIdPromise(id:number):Promise<Game> {
    return lastValueFrom(this.getGameById(id))
  }
}
