import React, { useState, useEffect } from 'react';
import apiClient from '@/services/api';
import LoadingSpinner from '@/components/LoadingSpinner/LoadingSpinner';
import type { Product } from '@/types';
import './ProductList.css';

interface ProductListProps {
  showLowStockOnly?: boolean;
}

export const ProductList: React.FC<ProductListProps> = ({ showLowStockOnly = false }) => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setLoading(true);
        setError(null);
        
        const response = showLowStockOnly 
          ? await apiClient.getProductsLowStock()
          : await apiClient.getProducts();
        
        if (response.success && response.data) {
          setProducts(response.data);
        } else {
          setError(response.error || 'Failed to fetch products');
        }
      } catch (err) {
        console.error('Error fetching products:', err);
        setError('Error connecting to server');
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [showLowStockOnly]);

  const handleStockUpdate = async (productId: number, quantidade: number) => {
    try {
      const response = await apiClient.updateProductStock(productId, quantidade);
      if (response.success && response.data) {
        // Update the product in local state
        setProducts(prev => 
          prev.map(product => 
            product.id === productId ? response.data! : product
          )
        );
      } else {
        alert('Failed to update stock: ' + response.error);
      }
    } catch (error) {
      console.error('Error updating stock:', error);
      alert('Error updating stock');
    }
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