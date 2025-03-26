import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../../enviroments/environment';
import { InvoiceItem, InvoiceItemDTO } from '../../models/invoice-item.model';
import { Product } from '../../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class InvoiceItemService {
  private apiUrl = `${environment.apiUrl}/itens`;

  constructor(private http: HttpClient) {}

  private toDTO(invoiceItem: InvoiceItem): InvoiceItemDTO {
    return {
      invoiceId: invoiceItem.invoiceId!,
      productId: invoiceItem.product.id,
      quantity: invoiceItem.quantity,
      unitPrice: invoiceItem.unitPrice
    };
  }

createInvoiceItem(itemData: InvoiceItemDTO): Observable<InvoiceItem> {
  return this.http.post<InvoiceItem>(this.apiUrl, itemData)
    .pipe(catchError(this.handleError));
}

  getInvoiceItemById(id: number): Observable<InvoiceItem> {
    return this.http.get<InvoiceItem>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  getAllInvoiceItems(): Observable<InvoiceItem[]> {
    return this.http.get<InvoiceItem[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  getItemsByInvoiceId(invoiceId: number): Observable<InvoiceItem[]> {
    return this.http.get<InvoiceItem[]>(`${this.apiUrl}/buscar-nf/${invoiceId}`)
      .pipe(catchError(this.handleError));
  }

  getItemsByProductId(productId: number): Observable<InvoiceItem[]> {
    return this.http.get<InvoiceItem[]>(`${this.apiUrl}/buscar-produto/${productId}`)
      .pipe(catchError(this.handleError));
  }

  updateInvoiceItem(id: number, itemData: InvoiceItemDTO): Observable<InvoiceItem> {
    return this.http.put<InvoiceItem>(`${this.apiUrl}/${id}`, itemData)
      .pipe(catchError(this.handleError));
  }

  deleteInvoiceItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  calculateTotal(item: InvoiceItem): number {
    return item.unitPrice * item.quantity;
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Ocorreu um erro desconhecido';
    
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erro: ${error.error.message}`;
    } else {
      if (error.error && error.error.message) {
        errorMessage = error.error.message;
      } else if (error.status === 400 || error.status === 404) {
        try {
          const errorBody = JSON.parse(error.error);
          errorMessage = errorBody.message || errorMessage;
        } catch (e) {}
      }
    }
    
    return throwError(() => new Error(errorMessage));
  }
}