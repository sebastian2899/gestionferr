import { Injectable } from '@angular/core';
import { SessionStorageService } from 'ngx-webstorage';

@Injectable({ providedIn: 'root' })
export class StateStorageService {
  private previousUrlKey = 'previousUrl';

  private facturaKey = 'facturaKey';

  private facturaCompraKey = 'facturaCompraKey';

  constructor(private sessionStorageService: SessionStorageService) {}

  storeUrl(url: string): void {
    this.sessionStorageService.store(this.previousUrlKey, url);
  }

  pasoParametroFactura(idFactura: number): void {
    this.sessionStorageService.store(this.facturaKey, idFactura);
  }

  getParametroFactura(): number | null {
    return this.sessionStorageService.retrieve(this.facturaKey) as number | null;
  }

  pasoParametroFacturaCompra(idFactura: number): void {
    this.sessionStorageService.store(this.facturaCompraKey, idFactura);
  }

  getParametroFacturaCompra(): number | null {
    return this.sessionStorageService.retrieve(this.facturaCompraKey) as number | null;
  }

  getUrl(): string | null {
    return this.sessionStorageService.retrieve(this.previousUrlKey) as string | null;
  }

  clearFactura(): void {
    this.sessionStorageService.clear(this.facturaKey);
  }

  clearFacturaCompra(): void {
    this.sessionStorageService.clear(this.facturaCompraKey);
  }

  clearUrl(): void {
    this.sessionStorageService.clear(this.previousUrlKey);
  }
}
