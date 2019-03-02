module Paths_mp3_cps (
    version,
    getBinDir, getLibDir, getDataDir, getLibexecDir,
    getDataFileName, getSysconfDir
  ) where

import qualified Control.Exception as Exception
import Data.Version (Version(..))
import System.Environment (getEnv)
import Prelude

catchIO :: IO a -> (Exception.IOException -> IO a) -> IO a
catchIO = Exception.catch

version :: Version
version = Version [0,1,0,0] []
bindir, libdir, datadir, libexecdir, sysconfdir :: FilePath

bindir     = "/home/alvin/Documents/school/uiuc/cs421/repo/assignments/mp3-cps/.stack-work/install/x86_64-linux/lts-3.17/7.10.2/bin"
libdir     = "/home/alvin/Documents/school/uiuc/cs421/repo/assignments/mp3-cps/.stack-work/install/x86_64-linux/lts-3.17/7.10.2/lib/x86_64-linux-ghc-7.10.2/mp3-cps-0.1.0.0-8Kpact7Y9qeG6n46X6WEAH"
datadir    = "/home/alvin/Documents/school/uiuc/cs421/repo/assignments/mp3-cps/.stack-work/install/x86_64-linux/lts-3.17/7.10.2/share/x86_64-linux-ghc-7.10.2/mp3-cps-0.1.0.0"
libexecdir = "/home/alvin/Documents/school/uiuc/cs421/repo/assignments/mp3-cps/.stack-work/install/x86_64-linux/lts-3.17/7.10.2/libexec"
sysconfdir = "/home/alvin/Documents/school/uiuc/cs421/repo/assignments/mp3-cps/.stack-work/install/x86_64-linux/lts-3.17/7.10.2/etc"

getBinDir, getLibDir, getDataDir, getLibexecDir, getSysconfDir :: IO FilePath
getBinDir = catchIO (getEnv "mp3_cps_bindir") (\_ -> return bindir)
getLibDir = catchIO (getEnv "mp3_cps_libdir") (\_ -> return libdir)
getDataDir = catchIO (getEnv "mp3_cps_datadir") (\_ -> return datadir)
getLibexecDir = catchIO (getEnv "mp3_cps_libexecdir") (\_ -> return libexecdir)
getSysconfDir = catchIO (getEnv "mp3_cps_sysconfdir") (\_ -> return sysconfdir)

getDataFileName :: FilePath -> IO FilePath
getDataFileName name = do
  dir <- getDataDir
  return (dir ++ "/" ++ name)
