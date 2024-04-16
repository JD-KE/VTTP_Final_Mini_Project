import { Component, OnDestroy, OnInit, Optional, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Game, GameCategory, GameRegion, GameStatus, GameSummary } from '../../model';
import { GameService } from '../../game.service';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { EventGameStore } from '../../event-game.store';
import { Subscription, lastValueFrom, map } from 'rxjs';
import { LoadingOverlayRef, LoadingService } from '../../loading.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrl: './game.component.css'
})
export class GameComponent implements OnInit, OnDestroy{
  private activatedRoute = inject(ActivatedRoute)
  private gameSvc = inject(GameService)
  private router = inject(Router)
  private data = inject(MAT_DIALOG_DATA, {optional:true})
  private eventGamesStore = inject(EventGameStore)
  private loadingSvc = inject(LoadingService)
  private dialog = inject(MatDialog)

  constructor() {
    
  }
  
  gameP$!:Promise<Game>
  id!:number
  idSub!:Subscription
  GameStatus=GameStatus
  GameCategory=GameCategory
  GameRegion=GameRegion
  isDialog=false
  inEventGameStoreSub!:Subscription
  inEventGameStore = false

  loadingRef!:LoadingOverlayRef
  
  ngOnInit(): void {
    // this.id = this.activatedRoute.snapshot.params['gameId']
    if(!!this.data) {
      this.isDialog = true
      this.id = this.data
      this.gameP$ = this.getGame(this.id)
    } else {
      this.idSub = this.activatedRoute.params.pipe(
        map(params => params['gameId'])
      ).subscribe(
        value => {
          // console.log(value)
          this.id = value
          this.gameP$ = this.getGame(this.id)
      })
    }
    

    this.inEventGameStoreSub = this.eventGamesStore.hasGame(this.id).subscribe(
      value => this.inEventGameStore = value
      )
    }

    ngOnDestroy(): void {
      this.inEventGameStoreSub.unsubscribe()
      this.idSub?.unsubscribe()
    }

  addGameToStore(game:Game) {
    this.eventGamesStore.addGameToEvent({
      id:game.id,
      name:game.name,
      coverUrl:game.coverUrl.replace("t_cover_big", "t_thumb")
    } as GameSummary)
  }

  removeGameFromStore(id:number) {
    this.eventGamesStore.removeGameFromEvent(id)
  }

  getGameDialog(id:number) {
    // console.log(id)
    const dialogRef = this.dialog.open(GameComponent, {
      data:id,
    })
  }

  private getGame(id:number):Promise<Game> {
    return this.gameSvc.getGameByIdPromise(this.id)
      .then(value =>{
        if(!!value.coverUrl){
          value.coverUrl = value.coverUrl.replace("t_thumb", "t_cover_big")
        }
        return value
      })
      .catch(error => {
        return {} as Game
      })
  }
}
