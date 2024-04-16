export interface GameResult{
    id:number,
    name:string,
    category:number,
    parent_game:Game,
    first_release_date:Date,
    coverUrl:string
    platforms:string
}

export interface GameResults{
    totalCount:number,
    results:GameResult[]
}

export interface GameSummary{
    id:number,
    name:string,
    coverUrl:string
}

export interface EventGames{
    games:GameSummary[]
}

export interface ReleaseDate{
    date:Date,
    human:string,
    platform:string,
    status:number,
    region:number
}

export interface Game{
    id:number,
    name:string,
    status:number,
    category:number,
    storyline:string,
    summary:string,
    first_release_date:Date,
    parent_game:Game,
    version_parent:Game,
    version_title:string,
    coverUrl:string,
    releaseDates:ReleaseDate[],
    platforms:string
}


export enum GameStatus {
    released=0,
    no_status=1,
    alpha=2,
    beta=3,
    early_access=4,
    offline=5,
    cancelled=6,
    rumored=7,
    delisted=8,
}

export enum GameCategory {
    main_game=0,
    dlc_addon=1,
    expansion=2,
    bundle=3,
    standalone_expansion=4,
    mod=5,
    episode=6,
    season=7,
    remake=8,
    remaster=9,
    expanded_game=10,
    port=11,
    fork=12,
    pack=13,
    update=14,
}

export enum GameMode {
    single_player=1,
    multiplayer=2,
    cooperative=3,
    split_screen=4,
    massively_multiplayer_online=5,
    battle_royale=6,
    
}

export enum GameRegion {
    europe=1,
    north_america=2,
    australia=3,
    new_zealand=4,
    japan=5,
    china=6,
    asia=7,
    worldwide=8,
    korea=9,
    brazil=10,
}

export enum GameReleaseStatus {
    alpha=1,
    beta=2,
    early_access=3,
    offline=4,
    cancelled=5,
    full_release=6,
}

export interface User{
    
    username:string
}

export interface UserRegister{
    username:string,
    email:string,
    password:string
}

export interface UserLogin{
    username:string,
    password:string
}

export interface EventModel{
    id:string
    name:string
    description:string
    details:string
    userCreated:string
    games:GameSummary[]
    startTime:Date
    endTime:Date
}

export interface EventResults{
    results:EventModel[]
    totalCount:number
}

export enum EventStatus{
    unknown=0,
    upcoming=1,
    ongoing=2,
    passed=3
}

export interface Tokens {
    access_token:string
    refresh_token:string
}