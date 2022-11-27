import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Product } from '@app/product/product';
import { GenericHttpService } from '@app/generic-http.service';

@Injectable({
  providedIn: 'root'
})

export class ProductService extends GenericHttpService<Product> {

  constructor(httpClient: HttpClient) {
    super(httpClient, `products`);
    
    } // constructor

  // delete(id: number): Observable<number> {
//   return this.http
//   .delete<number>(`${this.resourceURL}/${id}`)
//   .pipe(retry(1), catchError(this.handleError));
//   } // delete
}
