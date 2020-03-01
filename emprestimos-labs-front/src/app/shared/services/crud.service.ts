import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../page';

export abstract class CrudService<T, ID> {

  constructor(protected url: string, protected http: HttpClient) {}

  protected getUrl(): string {
    return this.url;
  }

  save(id: ID, t: T): Observable<T> {
    if (id) {
      return this.edit(id, t);
    } else {
      return this.insert(t);
    }
  }

  insert(t: T): Observable<T> {
    const url = `${this.getUrl()}`;
    return this.http.post<T>(url, t);
  }

  edit(id: ID, t: T): Observable<T> {
    const url = `${this.getUrl()}/${id}`;
    return this.http.put<T>(url, t);
  }

  findAll(): Observable<T[]> {
    const url = `${this.getUrl()}`;
    return this.http.get<T[]>(url);
  }

  findAllPaginated(page: number, size: number): Observable<Page<T>> {
    const url = `${this.getUrl()}/page`;

    let params = new HttpParams();
    params = params.set('page', page.toString());
    params = params.set('size', size.toString());

    return this.http.get<Page<T>>(url, {params});
  }

  findById(id: ID): Observable<T> {
    const url = `${this.getUrl()}/${id}`;
    return this.http.get<T>(url);
  }

  delete(id: ID) {
    const url = `${this.getUrl()}/${id}`;
    return this.http.delete(url);
  }

  count() {
    const url = `${this.getUrl()}/count`;
    return this.http.get(url);
  }
}
