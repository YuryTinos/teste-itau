import { TestBed } from '@angular/core/testing';

import { CervejaService } from './cerveja.service';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {Cerveja} from './cervejas/cerveja';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {environment} from '../environments/environment';

describe('CervejaService', () => {

  const url = `${environment.url}/cervejas`;

  const cervejasTeste: Cerveja[] = [
    { id: '1', nome: 'Teste1', pontuacao: 1 },
    { id: '2', nome: 'Teste2', pontuacao: 0 },
    { id: '3', nome: 'Teste3', pontuacao: 0 },
  ];

  let service: CervejaService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ]
    });
    service = TestBed.inject(CervejaService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('deverá retornar uma lista de cervejas', () => {

    service.listarCervejas().subscribe(data => {// When observable resolves, result should match test data
      expect(data).toHaveSize(3);
      expect(data).toEqual(cervejasTeste);

      expect(data[0].id).toEqual('1');
      expect(data[0].nome).toEqual('Teste1');
      expect(data[0].pontuacao).toEqual(1);
    });

    const req = httpTestingController.expectOne(url);

    // Assert that the request is a GET.
    expect(req.request.method).toEqual('GET');

    // Respond with mock data, causing Observable to resolve.
    // Subscribe callback asserts that correct data was returned.
    req.flush(cervejasTeste);

    // Finally, assert that there are no outstanding requests.
    httpTestingController.verify();
  });

  it('deverá adicionar uma cerveja a lista', () => {

    const cervejaTeste: Cerveja = {
      id: 'novo',
      nome: 'Teste Novo',
      pontuacao: 0
    };

    service.adicionarCerveja({ nome: 'Teste Novo' }).subscribe(data => {// When observable resolves, result should match test data
      expect(data).toEqual(cervejaTeste);

      expect(data.id).toEqual('novo');
      expect(data.nome).toEqual('Teste Novo');
      expect(data.pontuacao).toEqual(0);
    });

    const req = httpTestingController.expectOne(url);

    expect(req.request.method).toEqual('POST');

    req.flush(cervejaTeste);

    // Finally, assert that there are no outstanding requests.
    httpTestingController.verify();
  });

  it('deverá votar em uma cerveja da lista', () => {

    const cervejaTeste = {
      ...cervejasTeste[0],
      pontuacao: cervejasTeste[0].pontuacao + 1
    };

    service.votar(cervejaTeste).subscribe(data => {// When observable resolves, result should match test data
      expect(data).toEqual(cervejaTeste);
      expect(data.pontuacao).toEqual(cervejasTeste[0].pontuacao + 1);
    });

    const req = httpTestingController.expectOne(`${url}/${cervejaTeste.id}/votar`);

    expect(req.request.method).toEqual('POST');

    req.flush(cervejaTeste);

    // Finally, assert that there are no outstanding requests.
    httpTestingController.verify();
  });
});
