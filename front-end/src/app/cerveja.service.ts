import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from './../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Cerveja } from './cervejas/cerveja';
import { MensagemService } from './mensagem.service';

@Injectable({
  providedIn: 'root'
})
export class CervejaService {

  private cervejasUrl = `${environment.url}/cervejas`;

  constructor(private http: HttpClient,
              public mensagemService: MensagemService) { }

  listarCervejas(): Observable<Cerveja[]> {
    return this.http.get<Cerveja[]>(this.cervejasUrl)
      .pipe(
        tap(_ => console.log('Cervejas encontradas!')),
        catchError(this.handleError<Cerveja[]>('listarCervejas', []))
      );
  }

  adicionarCerveja(cerveja: Cerveja): Observable<Cerveja> {
    return this.http.post<Cerveja>(this.cervejasUrl, cerveja)
      .pipe(
        tap((novaCerveja: Cerveja) => console.log(`Cerveja adicionada com id=${novaCerveja.id}`)),
        catchError(this.handleError<Cerveja>('adicionarCerveja'))
      );
  }

  votar(cerveja: Cerveja): Observable<Cerveja> {
    return this.http.post(`${this.cervejasUrl}/${cerveja.id}/votar`, {})
      .pipe(
        tap((novaPontuacao: Cerveja) =>
          console.log(`Cerveja votada com pontuacao=${novaPontuacao.pontuacao}`)),
        catchError(this.handleError<Cerveja>('votar'))
      );
  }

  deletar(cerveja: Cerveja): Observable<Cerveja> {
    return this.http.delete<Cerveja>(`${this.cervejasUrl}/${cerveja.id}`, {})
      .pipe(
        tap(_ => console.log('Cerveja deletada!')),
        catchError(this.handleError<Cerveja>('deletar'))
      );
  }

  private handleError<T>(operation = 'operation', result?: T): (error: any) => Observable<T> {
    return (e: any): Observable<T> => {
      if (e.status === 0) {
        this.mensagemService.adicionar(`Falha de conexão com servidor!`, 'danger');
        return of(result as T);
      }
      this.mensagemService.adicionar(`${e.error.codigo}-${e.error.mensagem}`, 'danger');
      return of(result as T);
    };
  }
}
