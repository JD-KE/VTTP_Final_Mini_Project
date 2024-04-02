import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { SearchGamesComponent } from '../search-games/search-games.component';
import { EventGameStore } from '../../event-game.store';
import { Observable, Subscription } from 'rxjs';
import { EventBooking, GameSummary } from '../../model';
import { EventService } from '../../event.service';
import { GameComponent } from '../game/game.component';

@Component({
  selector: 'app-create-event',
  templateUrl: './create-event.component.html',
  styleUrl: './create-event.component.css'
})
export class CreateEventComponent implements OnInit, OnDestroy{
  private fb = inject(FormBuilder)
  private dialog = inject(MatDialog);
  private eventGamesStore = inject(EventGameStore)
  private eventSvc = inject(EventService)
  
  form!:FormGroup
  eventGames!:GameSummary[]
  eventGamesSub!:Subscription
  eventGames$!: Observable<GameSummary[]>
  hasEventGameSub!:Subscription
  hasGame!:boolean
  createEventSub!:Subscription
  
  ngOnInit(): void {
    this.form = this.createForm();
    this.eventGames$ = this.eventGamesStore.getEventGames
    this.eventGamesSub = this.eventGames$.subscribe(
      eventGames => {
        this.eventGames = eventGames
      }
    )
    this.hasEventGameSub = this.eventGamesStore.hasEventGames.subscribe(
      hasGame => this.hasGame = hasGame
      )
    }
  ngOnDestroy(): void {
    this.hasEventGameSub.unsubscribe()
  }
  
  getGame(id:number) {
    console.log(id)
    const dialogRef = this.dialog.open(GameComponent, {
      data:id,
    })
  }
    
  openSearch() {
    const dialogRef = this.dialog.open(SearchGamesComponent, {data:true})
  }

  deleteGameFromStore(id:number) {
    // console.log('delete')
    this.eventGamesStore.removeGameFromEvent(id)
  }

  processBooking() {
    console.log(this.form.value)
    var event:EventBooking = {
      ...this.form.value,
      startTime: new Date(this.form.get('startTime')?.value).getTime(),
      endTime: new Date(this.form.get('endTime')?.value).getTime(),
      games:this.eventGames
    } as EventBooking

    console.log(event.startTime)
    console.log(new Date(event.startTime).toDateString())
    console.log(new Date(event.startTime).toISOString())
    console.log(new Date(event.startTime).toLocaleString())
    
    this.createEventSub = this.eventSvc.createEvent(event).subscribe({

    
      next: value => console.log(value),
      complete: () => this.createEventSub.unsubscribe()
    })
  }

  createForm() {
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required,]),
      description: this.fb.control<string>('', [Validators.required,]),
      startTime: this.fb.control<string>('', [startDateLaterThanNow(),Validators.required] ),
      endTime: this.fb.control<string>('', [startDateLaterThanNow(),Validators.required,]),
      comments: this.fb.control<string>('')
    },
    {validators:endLaterThanStart})
  }

  
}

export function startDateLaterThanNow(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const controlValue = control.value;
    const currentDateTime = new Date();
    return (currentDateTime >= new Date(controlValue)) ? { laterThanOrEqualToCurrentDateTime: 
      { value: true} } : null;
  };
}

export const endLaterThanStart: ValidatorFn = (
  control: AbstractControl,
): ValidationErrors | null => {
  const startTime = control.get('startTime')?.value;
  const endTime = control.get('endTime')?.value;

  if(!!startTime && !!endTime) {
    const endLaterThanStart = (new Date(startTime).getTime() < new Date(endTime).getTime())
    return endLaterThanStart ? null : {endLaterThanStart: {value: false}}
  }

  return {startEndTimesEntered: {value:false}}
};