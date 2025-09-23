import React, { useState, useEffect } from 'react';
import apiClient from '@/services/api';
import LoadingSpinner from '@/components/LoadingSpinner';
import type { Produto } from '@/types';

interface Product {
  id: number;
  nome: string;
  preco: number;
  estoque: number;
  estoqueMinimo: number;
}

interface ProductListProps {
  showLowStockOnly?: boolean;
}

export const ProductList: React.FC<ProductListProps> = ({ showLowStockOnly = false }) => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    setLoading(true);
    setError(null);
    
    const onSuccess = (data: Product[]) => {
      setProducts(data);
      setLoading(false);
    };
    
    const onError = (errorMsg: string) => {
      setError(errorMsg || 'Failed to fetch products');
      setLoading(false);
    };
    
    if (showLowStockOnly) {
      apiClient.getProductsLowStock(onSuccess, onError);
    } else {
      apiClient.getProducts(onSuccess, onError);
    }
  }, [showLowStockOnly]);

  const handleStockUpdate = (productId: number, quantidade: number) => {
    // apiClient.updateProductStock(
    //   productId, 
    //   quantidade,
    //   (updatedProduct: Produto) => {
    //     // Update the product in local state
    //     const updatedProducts = products.map(product => 
    //       product.id === productId ? updatedProduct : product
    //     );
    //     setProducts(updatedProducts);
    //   },
    //   (errorMsg) => {
    //     alert('Failed to update stock: ' + errorMsg);
    //   }
    // );
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error) {
    return (
      <div className="error-message">
        <p>Error: {error}</p>
        <button onClick={() => window.location.reload()}>
          Try Again
        </button>
      </div>
    );
  }

  if (products.length === 0) {
    return (
      <div className="no-products">
        <p>{showLowStockOnly ? 'No products with low stock' : 'No products found'}</p>
      </div>
    );
  }

  return (
    <div className="product-list">
      <h2>{showLowStockOnly ? 'Products with Low Stock' : 'All Products'}</h2>
      <div className="products-grid">
        {products.map((product) => (
          <div key={product.id} className="product-card">
            <h3>{product.nome}</h3>
            <p className="price">R$ {product.preco.toFixed(2)}</p>
            <div className="stock-info">
              <span className={product.estoque <= product.estoqueMinimo ? 'low-stock' : ''}>
                Stock: {product.estoque}
              </span>
              <span className="min-stock">Min: {product.estoqueMinimo}</span>
            </div>
            
            <div className="stock-actions">
              <button 
                onClick={() => handleStockUpdate(product.id, 1)}
                className="stock-btn decrease"
              >
                -1
              </button>
              <button 
                onClick={() => handleStockUpdate(product.id, 5)}
                className="stock-btn decrease"
              >
                -5
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProductList;
