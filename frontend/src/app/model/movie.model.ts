export class Movie {
  id: string;
  title: string;
  genres: Genre[];
  directors: Person[];
  actors: Person[];
  rated: Rate[];
  released: number;
  tagline: string;
  averageRating: number;

  constructor(data: {
    id: string;
    title: string;
    genres: Genre[];
    directors: Person[];
    actors: Person[];
    rated: Rate[];
    released: number;
    tagline: string;
  }) {
    this.id = data.id;
    this.title = data.title;
    this.genres = data.genres;
    this.directors = data.directors;
    this.actors = data.actors;
    this.rated = data.rated;
    this.released = data.released;
    this.tagline = data.tagline;
    if (data.rated.length > 0) {
      this.averageRating = data.rated.reduce((sum, r) => sum + r.rating, 0) / data.rated.length;
    } else {
      this.averageRating = 0;
    }
  }
}

export interface MovieSearchCriteria {
  title: string;
  genre: string;
  year: number;
}

export interface RecommendationFilter {
  genre: string;
  year: number;
}

export interface Person {
  id: string;
  name: string;
  birthDate: number;
}

export interface Genre {
  id: string;
  name: string;
}

export interface Rate {
  movieId: string;
  userId: string;
  rating: number;
  user: User;
}

export interface User {
  id: string;
  email: string;
  role: string;
}
