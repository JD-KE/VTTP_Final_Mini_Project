import { Injectable } from "@angular/core";
import { EventGames, GameSummary } from "./model";
import { ComponentStore } from "@ngrx/component-store";

const INIT_STATE: EventGames = {
    games:[]
}

@Injectable()
export class EventGameStore extends ComponentStore<EventGames> {

    constructor() {
        super(INIT_STATE)
    }

    readonly addGameToEvent = this.updater<GameSummary>(
        (slice:EventGames, game:GameSummary) => {
            if(slice.games.findIndex((gameSum => gameSum.id == game.id)) == -1) {
                const newSlice:EventGames = {
                    games:[...slice.games, game]
                }
                return newSlice
            }
            alert('Game already in event, add operation cancelled')
            return slice
            
        }
    )

    readonly removeGameFromEvent = this.updater<number>(
        (slice:EventGames, id:number) => (
            {
                games: slice.games.filter(gameSum =>gameSum.id != id)
            }
        )
    )

    readonly clearEventGames = this.updater<void>(
        (slice:EventGames) => {
            const newSlice:EventGames = {
                games:[]
            }
            return newSlice
        }
    )

    readonly addAllEventGamesFromExistingEvent = this.updater<GameSummary[]>(
        (slice:EventGames, eventGames:GameSummary[] | null) => {
            const newSlice:EventGames = {
                games:eventGames || []
            }
            return newSlice
        }
    )

    readonly getEventGames = this.select(
        (slice:EventGames) => slice.games
    )

    readonly hasEventGames = this.select(
        (slice:EventGames) => slice.games.length > 0
    )

    readonly hasGame = (id:number) =>
        this.select(
            (slice:EventGames) => {
                return (slice.games.findIndex((gameSum => gameSum.id == id)) != -1)
            }
        )

}