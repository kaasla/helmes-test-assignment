import { useCallback, useEffect, useState } from "react";

type ToastType = "success" | "error";

interface ToastState {
  readonly message: string;
  readonly type: ToastType;
  readonly visible: boolean;
}

const TOAST_DURATION = 3000;

export const useToast = () => {
  const [toast, setToast] = useState<ToastState>({ message: "", type: "success", visible: false });

  const show = useCallback((message: string, type: ToastType = "success") => {
    setToast({ message, type, visible: true });
  }, []);

  useEffect(() => {
    if (!toast.visible) return;

    const timer = setTimeout(() => {
      setToast((prev) => ({ ...prev, visible: false }));
    }, TOAST_DURATION);

    return () => {
      clearTimeout(timer);
    };
  }, [toast.visible, toast.message]);

  return { toast, show };
};
