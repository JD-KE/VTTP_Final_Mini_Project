import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GameService } from '../../game.service';
import { GameCategory, GameResult } from '../../model';
import { PageEvent } from '@angular/material/paginator';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { GameComponent } from '../game/game.component';
import { Observable, Subject, Subscription, debounceTime, distinctUntilChanged, lastValueFrom, map, switchMap } from 'rxjs';

@Component({
  selector: 'app-search-games',
  templateUrl: './search-games.component.html',
  styleUrl: './search-games.component.css'
})
export class SearchGamesComponent implements OnInit, OnDestroy{
  
  private fb = inject(FormBuilder);
  private gameSvc = inject(GameService);
  private dialog = inject(MatDialog);
  private data = inject(MAT_DIALOG_DATA, {optional:true})
  
  form!:FormGroup;
  searchTerm!:string;
  resultsLength!:number;
  gameResultsSub!:Subscription
  gameResultsP$!:Promise<GameResult[]>;
  GameCategory=GameCategory;
  private searchText$ = new Subject<string>();
  
  pageSize:number = 10;
  pageIndex:number = 0;
  pageSizeOptions = [5, 10, 25];
  pageEvent!:PageEvent;
  isDialog = false
  
  ngOnInit(): void {
    this.form = this.createForm()
    this.isDialog = this.data
    this.gameResultsSub = this.searchText$.pipe(
      debounceTime(1000),
      distinctUntilChanged()
      ).subscribe(
        value => {
          this.search()
        }
        )
      }
      
  ngOnDestroy(): void {
    this.gameResultsSub?.unsubscribe()
  }

  storeNewSearch() {
    this.searchTerm = this.form.value['search'];
    this.pageIndex = 0;
    this.search();
  }
      
  search() {
    this.gameResultsP$ = this.gameSvc.getGamesResultsPromise(this.searchTerm, this.pageIndex+1, this.pageSize)
      .then(
        value => {
          this.resultsLength = value.totalCount
          return value.results;
        }
      )
  }

  searchGame(searchTerm:string) {
    this.searchTerm = searchTerm
    // console.log(searchTerm)
    this.pageIndex = 0
    this.searchText$.next(searchTerm)
  }

  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.pageIndex = e.pageIndex;
    this.pageSize = e.pageSize
    if(!!this.searchTerm) this.search();
  }

  getGame(id:number) {
    console.log(id)
    const dialogRef = this.dialog.open(GameComponent, {
      data:id,
    })
  }
  
  private createForm(): FormGroup {
    return this.fb.group({
      search: this.fb.control<string>('', [ Validators.required ]),
    })
  }

  getValue(event: Event): string {
    return (event.target as HTMLInputElement).value;
  }

}
