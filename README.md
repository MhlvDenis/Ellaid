# Ellaid
![](docs/images/arch.png)

## Installation

### Dependencies
- JDK 17
- Docker
- Python 3

### Run backend locally
```shell
cd backend
cp .env.example .env
python3 run.py build image run all -d
```

## Endpoints

| Method | Path                        | Description                               | Authorization |
|:------:|:----------------------------|-------------------------------------------|:-------------:|
|  POST  | /auth/sign-up               | Add new user                              |               |
|  POST  | /auth/sign-in               | Login as existed user                     |               |
|  GET   | /auth/validate              | Validate auth token                       |               |
|  POST  | /comment                    | Leave new comment for track               |     USER      |
| PATCH  | /comment                    | Edit comment                              |     USER      |
| DELETE | /comment                    | Delete comment                            |     USER      |
|  GET   | /comments                   | Get all comments for track                |               |
|  GET   | /playlist                   | Get playlist                              |               |
|  GET   | /playlists                  | Get all playlists for user                |               | 
|  POST  | /playlist                   | Create new playlist                       |     USER      |
| DELETE | /playlist                   | Delete playlist                           |     USER      |
| PATCH  | /playlist/add-track         | Add track to playlist                     |     USER      |
| PATCH  | /playlist/remove-track      | Remove track from playlist                |     USER      |
|  GET   | /search                     | Get relevant tracks by pattern            |               |
|  POST  | /storage-api/upload         | Upload file to storage and get url for it |     ADMIN     |
|  GET   | /storage-api/download/{url} | Download file by url                      |               |
|  GET   | /track                      | Get track metadata                        |               |
|  POST  | /track                      | Add new track                             |     ADMIN     |
